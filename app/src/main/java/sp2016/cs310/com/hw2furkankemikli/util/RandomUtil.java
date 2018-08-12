package sp2016.cs310.com.hw2furkankemikli.util;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class RandomUtil {
    public static String generateNoteId(){
        return String.valueOf((int)(Math.random()*100000000));
    }
    public static int generateRandomIndex()
    {
        return (int)(Math.random()*10);
    }
}
