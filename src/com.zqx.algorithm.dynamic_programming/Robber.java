package com.zqx.algorithm.dynamic_programming;

public class Robber {


    /**
     * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
     * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
     * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
     *
     * @param nums 每间房子里面拥有的金额
     * @return 偷取到的总金额
     */
    public static int rob(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        if (nums.length == 1) {
            return nums[0];
        }

        //存储抢劫每一家时获得的金额数组， 比如 money[0]表示抢劫第一家后获得的金额， money[n]表示抢劫第n-1家时获得的总金额
        int[] money = new int[nums.length];
        money[0] = nums[0];
        money[1] = Math.max(nums[0], nums[1]);
        money[2] = Math.max(money[1], nums[0] + nums[2]);

        for (int i = 3; i < nums.length; i++) {
            //关键思路： 偷到第i户人家获取的最大金额 = 第i户人家金额 + max(i-2户获取到的最大金额 ， i-3户获取到的最大金额)
            //因为偷第i户就不能偷i-1户， 偷完i-2或者i-3就可以偷i户。
            money[i] = Math.max(money[i-2], money[i-3]) + nums[i];
        }

        return Math.max(money[nums.length-2],money[nums.length - 1]);
    }

    /**
     * 网上找到的实现。 如果仅仅返回金额，方法更简洁
     * @param nums
     * @return
     */
    public static int rob2(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return nums[0];
        }
        int pre3 = 0, pre2 = 0, pre1 = 0;
        for (int i = 0; i < n; i++) {
            int cur = Math.max(pre2, pre3) + nums[i];
            pre3 = pre2;
            pre2 = pre1;
            pre1 = cur;
        }
        return Math.max(pre1, pre2);
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,7,9,3,1};
        int robMoney = rob(nums);
        System.out.println("robMoney = " + robMoney);
    }

}
