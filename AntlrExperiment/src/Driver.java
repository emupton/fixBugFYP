
public class Driver {
	public static void main (String args[]){
		System.out.println("Result:" + isPrime(7));
		
	}
	
	public static boolean isPrime(int n){
		
		int maxFactor = n/2 + 1;
		
		for(int i=maxFactor; i>1; i--){
			if(n % ((double) i )==0){
				return false;
			}
		}
		
		return true;
	}
}
