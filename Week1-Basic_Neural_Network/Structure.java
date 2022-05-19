import java.util.ArrayList;

public class Structure extends ArrayList<Integer>
{
    //feature
    private String description;
    
    //constructor
    //description: 2,2,1
    public Structure ( String description )
    {
        String [] parts = description.split ( "," );
        
        for ( int pI = 0; pI < parts.length; pI ++ )
            add ( Integer.parseInt ( parts [ pI ] ) );
       
    }
}
