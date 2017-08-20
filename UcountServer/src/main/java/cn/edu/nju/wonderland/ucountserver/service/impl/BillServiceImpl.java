package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SchoolCardRepository;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
	private IcbcCardRepository icbcCardRepository;
	private SchoolCardRepository schoolCardRepository;
	private AlipayRepository alipayRepository;

	@Override
	public BillInfoVO getBill(Long accountId, Long billId) {
		BillInfoVO billInfoVO = new BillInfoVO();
		if (icbcCardRepository.findByIdAndCardId(billId, String.valueOf(accountId)) != null) {
			IcbcCard icbcCard = icbcCardRepository.findByIdAndCardId(billId, String.valueOf(accountId));
			billInfoVO.trader = icbcCard.getOtherAccount();
			billInfoVO.tradeDate = icbcCard.getTradeDate();
			if (icbcCard.getAccountAmountIncome() > 0) {
				billInfoVO.type = "收入";
				billInfoVO.amount = icbcCard.getAccountAmountIncome();
			} else {
				billInfoVO.type = "支出";
				billInfoVO.amount = icbcCard.getAccountAmountExpense();
			}
		} else if (schoolCardRepository.findByIdAndCardId(billId, String.valueOf(accountId)) != null) {
			SchoolCard schoolCard = schoolCardRepository.findByIdAndCardId(billId, String.valueOf(accountId));
			billInfoVO.tradeDate = schoolCard.getTime();
			billInfoVO.trader = schoolCard.getLocation();
			billInfoVO.amount = schoolCard.getIncomeExpenditure();
			if (billInfoVO.amount > 0) {
				billInfoVO.type = "收入";
			} else {
				billInfoVO.type = "支出";
			}
		} else {
			Alipay alipay = alipayRepository.findByIdAndCardId(billId, String.valueOf(accountId));
			billInfoVO.amount = alipay.getMoney();
			billInfoVO.type = alipay.getIncomeExpenditureType();
			billInfoVO.tradeDate = alipay.getCreateTime();
			billInfoVO.trader = alipay.getTrader();
		}
		return billInfoVO;
	}

	@Override
	public List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable) {
		List<BillInfoVO> billInfoVOList = new ArrayList<BillInfoVO>();
		if (icbcCardRepository.findByCardId(String.valueOf(accountId), pageable) != null) {
			List<IcbcCard> icbcCardList = icbcCardRepository.findByCardId(String.valueOf(accountId), pageable);
			for (int i = 0; i < icbcCardList.size(); i++) {
				BillInfoVO billInfoVO = new BillInfoVO();
				billInfoVO.trader = icbcCardList.get(i).getOtherAccount();
				billInfoVO.tradeDate = icbcCardList.get(i).getTradeDate();
				if (icbcCardList.get(i).getAccountAmountIncome() > 0) {
					billInfoVO.type = "收入";
					billInfoVO.amount = icbcCardList.get(i).getAccountAmountIncome();
				} else {
					billInfoVO.type = "支出";
					billInfoVO.amount = icbcCardList.get(i).getAccountAmountExpense();
				}
				billInfoVOList.add(billInfoVO);
			}

		} else if (schoolCardRepository.findByCardId(String.valueOf(accountId), pageable) != null) {
			List<SchoolCard> schoolCardList = schoolCardRepository.findByCardId(String.valueOf(accountId), pageable);
			for (int i = 0; i < schoolCardList.size(); i++) {
				BillInfoVO billInfoVO = new BillInfoVO();
				billInfoVO.tradeDate = schoolCardList.get(i).getTime();
				billInfoVO.trader = schoolCardList.get(i).getLocation();
				billInfoVO.amount = schoolCardList.get(i).getIncomeExpenditure();
				if (billInfoVO.amount > 0) {
					billInfoVO.type = "收入";
				} else {
					billInfoVO.type = "支出";
				}
				billInfoVOList.add(billInfoVO);
			}
		} else {
			List<Alipay> alipayList = alipayRepository.findByCardID(String.valueOf(accountId), pageable);
			for (int i = 0; i < alipayList.size(); i++) {
				BillInfoVO billInfoVO = new BillInfoVO();
				billInfoVO.amount = alipayList.get(i).getMoney();
				billInfoVO.type = alipayList.get(i).getIncomeExpenditureType();
				billInfoVO.tradeDate = alipayList.get(i).getCreateTime();
				billInfoVO.trader = alipayList.get(i).getTrader();
				billInfoVOList.add(billInfoVO);
			}

		}
		return billInfoVOList;
	}

	@Override
	public List<BillInfoVO> getBillsByUser(String username, Pageable pageable) {
		List<BillInfoVO> billInfoVOList = new ArrayList<BillInfoVO>();
		List<IcbcCard> icbcCardList = icbcCardRepository.findByUsername(username, pageable);
		for (int i = 0; i < icbcCardList.size(); i++) {
			BillInfoVO billInfoVO = new BillInfoVO();
			billInfoVO.trader = icbcCardList.get(i).getOtherAccount();
			billInfoVO.tradeDate = icbcCardList.get(i).getTradeDate();
			if (icbcCardList.get(i).getAccountAmountIncome() > 0) {
				billInfoVO.type = "收入";
				billInfoVO.amount = icbcCardList.get(i).getAccountAmountIncome();
			} else {
				billInfoVO.type = "支出";
				billInfoVO.amount = icbcCardList.get(i).getAccountAmountExpense();
			}
			billInfoVOList.add(billInfoVO);
		}
		List<SchoolCard> schoolCardList = schoolCardRepository.findByUsername(username, pageable);
		for (int i = 0; i < schoolCardList.size(); i++) {
			BillInfoVO billInfoVO = new BillInfoVO();
			billInfoVO.tradeDate = schoolCardList.get(i).getTime();
			billInfoVO.trader = schoolCardList.get(i).getLocation();
			billInfoVO.amount = schoolCardList.get(i).getIncomeExpenditure();
			if (billInfoVO.amount > 0) {
				billInfoVO.type = "收入";
			} else {
				billInfoVO.type = "支出";
			}
			billInfoVOList.add(billInfoVO);
		}
		List<Alipay> alipayList = alipayRepository.findByUsername(username, pageable);
		for (int i = 0; i < alipayList.size(); i++) {
			BillInfoVO billInfoVO = new BillInfoVO();
			billInfoVO.amount = alipayList.get(i).getMoney();
			billInfoVO.type = alipayList.get(i).getIncomeExpenditureType();
			billInfoVO.tradeDate = alipayList.get(i).getCreateTime();
			billInfoVO.trader = alipayList.get(i).getTrader();
			billInfoVOList.add(billInfoVO);
		}
		return billInfoVOList;
	}

	@Override
	public Long addBillManually(Long accountId, BillAddVO billAddVO) {
		return null;
	}

	@Override
	public List<BillInfoVO> addBillAutomatically(Long accountId) {
		return null;
	}

	@Override
	public void deleteBill(Long accountId, Long billId) {
		if(icbcCardRepository.findByIdAndCardId(billId, String.valueOf(accountId)) != null){
			icbcCardRepository.delete(billId);
		}else if(schoolCardRepository.findByIdAndCardId(billId, String.valueOf(accountId)) != null) {
			schoolCardRepository.delete(billId);
		}else{
			alipayRepository.delete(billId);
		}
	}
}
