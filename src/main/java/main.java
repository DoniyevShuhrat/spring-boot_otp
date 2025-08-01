import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        int[] nums = {3,2,3};
        int target = 6;

        Solution solution = new Solution();
        int[] result = solution.twoSum(nums, target);
        System.out.println(result[0] + " " + result[1]);

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