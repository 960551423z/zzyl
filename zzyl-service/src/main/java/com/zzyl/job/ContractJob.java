package com.zzyl.job;

import cn.hutool.core.collection.CollUtil;
import com.zzyl.entity.Contract;
import com.zzyl.enums.ContractStatusEnum;
import com.zzyl.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同管理
 * @author 阿庆
 */
@Slf4j
@Component
public class ContractJob {

    @Resource
    ContractService contractService;

    /**
     * 合同状态更新
     */
    @Scheduled(cron = "0 0 * * * *")
    public void contractJob() {

        List<Contract> contractList = contractService.listAllContracts();
        if (CollUtil.isNotEmpty(contractList)) {
            List<Contract> updateList = new ArrayList<>();
            List<Contract> effUpdateList = new ArrayList<>();
            for (Contract contract : contractList) {
                if (contract.getStatus().equals(ContractStatusEnum.UN_EFFECTIVE.getOrdinal())) {
                    continue;
                }
                if (contract.getEndTime().isBefore(LocalDateTime.now())) {
                    contract.setStatus(ContractStatusEnum.EXPIRED.getOrdinal());
                    updateList.add(contract);
                } else if (contract.getStartTime().isBefore(LocalDateTime.now())  && contract.getEndTime().isAfter(LocalDateTime.now())) {
                    contract.setStatus(ContractStatusEnum.EFFECTIVE.getOrdinal());
                    effUpdateList.add(contract);
                }
            }
            if (CollUtil.isNotEmpty(updateList)) {
                contractService.updateBatchById(updateList);
            }
            if (CollUtil.isNotEmpty(updateList)) {
                contractService.updateBatchById(effUpdateList);
            }
        }


    }

}
