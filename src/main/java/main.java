import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        int[] nums = {3,2,3};
        int target = 6;

//        Solution solution = new Solution();
//        int[] result = solution.twoSum(nums, target);
//        System.out.println(result[0] + " " + result[1]);
        String msgText = "Zabon ilovasida ro'yxatdan o'tish uchun kodingiz code: 09804 Ushbu kodni hech kimga bermang! Vebsayt: https://zabon.app";
        Pattern pattern = Pattern.compile("code:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(msgText);

        if (matcher.find()) {
            String code = matcher.group(1);
            System.out.println("Kodni ajratib oldik: " + code);
        } else {
            System.out.println("Kod topilmadi");
        }


    }
}
class Solution {
    public int[] twoSum(int[] nums, int target) {

        Map<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++){
            int compement = target - nums[i];
//            System.out.println(numMap);

            if(numMap.containsKey(compement)){
                return new int[]{numMap.get(compement), i};
            }

            numMap.put(nums[i], i);
        }
        return new int[]{0, 0};
    }
}