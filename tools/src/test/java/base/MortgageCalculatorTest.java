package base;

/**
 * @author Jimmy
 * 房贷提前还款计算器
 * https://www.kjson.com/money/tqloans/
 */
public class MortgageCalculatorTest {

    public static void main(String[] args) {
        double totalAmount = 75 * 10000.0; // 贷款本金
        int term = 30 * 12; // 还款期限，单位是月
        double annualRate = 6.46; // 年利率（%）：
        String repaymentMethod = "等额本息"; // 贷款方式，可以是"等额本息"或"等额本金"

        int paidTerm = 52; //请输入当前已还期数


        double extraPrincipal = 10 * 10000.0; // 提前还款金额
        int extraTerm = term - paidTerm - 7 * 12 ; // 提前还款期限，单位是月
        //double extraMonthlyPayment  = 1100.0; // 调整后每月还款金额
        double extraRate = 6.46;

        // 将年利率转化为月利率，并计算每月还款金额
        double monthlyRate = annualRate / 12 / 100;
        double monthlyPayment = 0;
        if (repaymentMethod.equals("等额本息")) {
            monthlyPayment = totalAmount * monthlyRate * Math.pow(1 + monthlyRate, term) /
                    (Math.pow(1 + monthlyRate, term) - 1);
        } else if (repaymentMethod.equals("等额本金")) {
            monthlyPayment = totalAmount / (term) + totalAmount * monthlyRate;
        }

        // 计算已支付利息和剩余未支付本金
        double totalInterest = 0;
        double remainingPrincipal = totalAmount;
        for (int i = 0; i < paidTerm; i++) {
            double interest = remainingPrincipal * monthlyRate;
            double payment = monthlyPayment - interest;
            totalInterest += interest;
            remainingPrincipal -= payment;
        }

        // 计算额外还款导致的总支付利息
        double extraTotalInterest = 0;
        double extraMonthlyRate = extraRate / 12 / 100;
        double extraTotalPrincipal = remainingPrincipal - extraPrincipal;
        double extraRemainingPrincipal = extraTotalPrincipal;
        if (extraTerm > 0 && extraPrincipal > 0) {
            // 提前还款后的每月还款金额
            double extraMonthlyPayment = (extraRemainingPrincipal) * extraMonthlyRate * Math.pow(1 + extraMonthlyRate, extraTerm) /
                    (Math.pow(1 + extraMonthlyRate, extraTerm) - 1);
            for (int i = 0; i < extraTerm; i++) {
                double extraInterest = extraRemainingPrincipal * extraMonthlyRate;
                double extraPayment = extraMonthlyPayment - extraInterest;
                if (extraRemainingPrincipal < extraPayment) {
                    extraPayment = extraRemainingPrincipal + extraInterest;
                }
                extraTotalInterest += extraInterest;
                extraRemainingPrincipal -= extraPayment;
                if (extraRemainingPrincipal <= 0) {
                    break;
                }
            }
        }

        // 计算提前还款后的每月还款金额
        double newMonthlyPayment = 0;
        if (extraTotalPrincipal > 0) {
            double newMonthlyRate = annualRate / 12 / 100;
            if (repaymentMethod.equals("等额本息")) {
                newMonthlyPayment = extraTotalPrincipal * newMonthlyRate *
                        Math.pow(1 + newMonthlyRate, extraTerm) /
                        (Math.pow(1 + newMonthlyRate, extraTerm) - 1);
            } else if (repaymentMethod.equals("等额本金")) {
                newMonthlyPayment = extraTotalPrincipal / extraTerm + extraTotalPrincipal * newMonthlyRate;
            }
        }

        // 输出结果
        System.out.println("当前已还期数：" + paidTerm + "期");
        System.out.println("当前已支付利息：" + totalInterest + "元");
        System.out.println("当前剩余未支付本金：" + remainingPrincipal + "元");
        if (extraTerm > 0 && extraPrincipal > 0) {
            System.out.println("额外还款后需要还款期数：" + extraTerm + "期");
            System.out.println("额外还款后总支付利息：" + (extraTotalInterest + totalInterest - totalInterest * paidTerm / (term)) + "元");
            System.out.println("额外还款后每月还款金额：" + newMonthlyPayment + "元");
        } else {
            System.out.println("当前需要还款期数：" + (term - paidTerm) + "期");
            System.out.println("当前总支付利息：" + totalInterest + "元");
            System.out.println("当前每月还款金额：" + monthlyPayment + "元");
        }
    }

}



