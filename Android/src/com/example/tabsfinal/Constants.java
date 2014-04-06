package com.example.tabsfinal;

//ES - This class is take directly from the highscores sample. The random names is not used here.

import java.util.Random;


public class Constants {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This sample App is for demonstration purposes only.
    // It is not secure to embed your credentials into source code.
    // Please read the following article for getting credentials
    // to devices securely.
    // http://aws.amazon.com/articles/Mobile/4611615499399490
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
        public static final String ACCESS_KEY_ID = "AKIAJWQU5ZV4ZEZHRDWA";
        public static final String SECRET_KEY = "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";
    
    public static final Random random = new Random();
    public static final String[] NAMES = new String[] { "Norm", "Jim", "Jason", "Zach", "Matt", "Glenn", "Will", "Wade", "Trevor", "Jeremy", "Ryan", "Matty", "Steve", "Pavel" };

    
    public static String getRandomPlayerName() {
        int name1 = random.nextInt( NAMES.length );
        int name2 = random.nextInt( NAMES.length );
        
        return NAMES[name1] + " " + NAMES[name2];
    }
    
    public static int getRandomScore() {
        return random.nextInt( 1000 ) + 1;
    }
}