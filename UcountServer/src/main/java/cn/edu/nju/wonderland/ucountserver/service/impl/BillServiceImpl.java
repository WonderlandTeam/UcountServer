package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;

import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {
	private IcbcCardRepository icbcCardRepository;
	private SchoolCardRepository schoolCardRepository;
	private AlipayRepository alipayRepository;
	private ManualBillingRepository manualBillingRepository;
	private AccountRepository accountRepository;
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
			List<Alipay> alipayList = alipayRepository.findByCardId(String.valueOf(accountId), pageable);
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
		Map<Integer,List<IcbcCard>> icbcCardmap = new HashMap<>();
		/* 工行卡的表*/
		List<SchoolCard> schoolCardList = schoolCardRepository.findByUsername(username, pageable);
		List<Alipay> alipayList = alipayRepository.findByUsername(username, pageable);
		List<Account> accounts = accountRepository.findByUsername(username);
		for(int i = 0 ; i < accounts.size() ; i ++) {
			if(!accounts.get(i).getCardType().contains("银行卡")){
				accounts.remove(i);//选出银行卡
			}
		}
		for ( int i = 0 ; i < accounts.size();i++){
			icbcCardmap.put(i,icbcCardRepository.findByCardId(accounts.get(i).getCardId(),pageable));
		}
		for(int k = 0 ; k < accounts.size();k++) {
			List<IcbcCard> icbcCardList = icbcCardmap.get(k);
			for (int i = 0; i < icbcCardList.size(); i++) {
				for (int j = 0; j < alipayList.size(); j++) {
					if (icbcCardList.get(i).getAccountAmountExpense() == alipayList.get(j).getMoney()
							&& icbcCardList.get(i).getTradeDate() == alipayList.get(j).getPayTime()) {
						if (alipayList.get(j).getCommodity().equals("充值-普通充值")) {
							alipayList.remove(j);
							icbcCardList.remove(i);//支付宝从卡转账到余额，
						} else {
							alipayRepository.delete(alipayList.get(j));
							alipayList.remove(j);//支付宝用银行卡消费
						}
					} else if ((alipayList.get(j).getMoney() - icbcCardList.get(i).getAccountAmountIncome() < 10)
							&& (icbcCardList.get(i).getTradeDate().getTime() - alipayList.get(j).getPayTime().getTime() <= (2 * 60 * 1000))
							&& (alipayList.get(j).getCommodity().equals("提现-快速提现"))) {
						//支付宝余额提现到银行卡
						alipayList.remove(j);
						icbcCardList.remove(i);
						BillInfoVO billInfoVO = new BillInfoVO();
						billInfoVO.trader = "支付宝提现";
						billInfoVO.amount = alipayList.get(j).getMoney() - icbcCardList.get(i).getAccountAmountIncome();
						billInfoVO.tradeDate = alipayList.get(i).getPayTime();
						billInfoVO.type = "支付宝提现";
						billInfoVOList.add(billInfoVO);
					}
				}
				for (int j = 0; j < schoolCardList.size(); j++) {
					if (icbcCardList.get(i).getAccountAmountExpense() == schoolCardList.get(j).getIncomeExpenditure() &&
							icbcCardList.get(i).getTradeDate() == schoolCardList.get(j).getTime()) {
						schoolCardList.remove(j);
						icbcCardList.remove(i);//银行卡转账到校园卡
					}
				}
			}
		}
		for(int k = 0 ; k < accounts.size();k++) {
			List<IcbcCard> icbcCardList = icbcCardmap.get(k);
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
		}
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
		ManualBilling manualBilling = new ManualBilling();
		manualBilling.setCardId(String.valueOf(accountId));
		manualBilling.setCardType(billAddVO.cardType);
		manualBilling.setCommodity(billAddVO.commodity);
		manualBilling.setConsumeType(billAddVO.consumeType);
		manualBilling.setIncomeExpenditure(billAddVO.incomeExpenditure);
		manualBilling.setTime(billAddVO.time);
		manualBilling.setUsername(billAddVO.username);
		manualBilling.setRemark(billAddVO.remark);
		manualBilling = manualBillingRepository.save(manualBilling);
		return manualBilling.getId();
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

	@Override
	public double getConsumedMoneyByTypeAndTime(String username, String consumeType, String time) {
		DateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH / mm / ss");
		Date startDate  = new Date();
		Date endDate = new Date();
		Timestamp start = new Timestamp(0);
		Timestamp end = new Timestamp(0);
		double result = 0;
		TotalAccountVO totalAccountVO = new TotalAccountVO();
		try {
			startDate = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			endDate = calendar.getTime();
			start = new Timestamp(startDate.getTime());
			end = new Timestamp(endDate.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Alipay> alipays = alipayRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <alipays.size();i++){
			if(alipays.get(i).getConsumeType().equals(consumeType)){
				result += alipays.get(i).getMoney();
			}
		}
		List<SchoolCard> schoolCards =schoolCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <schoolCards.size();i++){
			if(schoolCards.get(i).getConsumeType().equals(consumeType)){
				if(schoolCards.get(i).getIncomeExpenditure() > 0 ) {
					result += schoolCards.get(i).getIncomeExpenditure();
				}else {
					result -= schoolCards.get(i).getIncomeExpenditure();
				}
			}
		}
		List<IcbcCard> icbcCards = icbcCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <icbcCards.size();i++){
			if(icbcCards.get(i).getConsumeType().equals(consumeType)){
				if(icbcCards.get(i).getAccountAmountExpense() > 0 ){
					result += icbcCards.get(i).getAccountAmountExpense();
				}else{
					result += icbcCards.get(i).getAccountAmountIncome();
				}
			}
		}
		return result;
	}
}
