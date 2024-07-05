package com.zzyl.aspect;

import cn.hutool.json.JSONUtil;
import com.zzyl.base.BaseDto;
import com.zzyl.base.DataScope;
import com.zzyl.utils.NoProcessing;
import com.zzyl.utils.StringUtils;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.RoleVo;
import com.zzyl.vo.UserVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * DataScopeAspect
 * @author 阿庆
 **/
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "0";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "1";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "4";


    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.zzyl.base.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(subject, UserVo.class);
        // 如果是超级管理员，则不过滤数据
        if (StringUtils.isNotNull(userVo) && !userVo.getUsername().equals("admin")) {
            dataScopeFilter(joinPoint, userVo, controllerDataScope.deptAlias(),
                    controllerDataScope.userAlias());
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, UserVo user, String deptAlias, String userAlias) {
        System.out.println("过滤数据---------------");
        StringBuilder sqlString = new StringBuilder();

        for (RoleVo role : user.getRoleList()) {
            String dataScope = role.getDataScope();//拥有的数据权限
            // 如果是全部数据权限，则不过滤数据
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
                // 如果是自定数据权限，则只查看自己的数据
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(" OR dept_no IN ( SELECT dept_no FROM sys_role_dept WHERE role_id = " + role.getId() + " ) ");
                // 如果是部门数据权限，则只查看本部门数据
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(" OR dept_no = " + user.getDeptNo() + " ");
                // 如果是部门及以下数据权限，则查看本部门以及下级数据
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                String str = NoProcessing.processString(user.getDeptNo()) + "%";
                sqlString.append(
                        " OR dept_no IN ( SELECT dept_no FROM sys_dept WHERE dept_no = " + user.getDeptNo() + " or dept_no like  '" + str + "')");
                // 如果是仅本人数据权限，则只查看本人的数据
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {//  or u.user_id = 登录用户id
                sqlString.append(" OR create_by = " + user.getId());
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];  //获取第一个参数  要求一定得是一个BaseEntity  在Service执行前 则就已经加上了 Sql   or u.user_id = 登录用户id
            if (StringUtils.isNotNull(params) && params instanceof BaseDto) {
                BaseDto baseDto = (BaseDto) params;
                baseDto.getParams().put(DATA_SCOPE, "(" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
