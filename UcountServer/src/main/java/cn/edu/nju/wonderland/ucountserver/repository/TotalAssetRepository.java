package cn.edu.nju.wonderland.ucountserver.repository;

import cn.edu.nju.wonderland.ucountserver.entity.TotalAsset;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 账户总资产（用户名，账户类型，账号，时间，收/支，消费项目，消费类型，余额，流入累计，流出累计，备注）
 * Created by green-cherry on 2017/8/17.
 */
@Deprecated
public interface TotalAssetRepository  extends JpaRepository<TotalAsset,Long> {
}
