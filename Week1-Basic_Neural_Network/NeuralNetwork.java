public class NeuralNetwork
{
    //features
    private double eta = 0.2;
    private double alpha = 0.5;
    private Structure structure = new Structure ( "2,2,1" );
    private Layers layers;
 
    //constructor
    public NeuralNetwork ( )
    {
        layers = new Layers ( );
        
        for ( int lSI = 0; lSI < structure.size ( ); lSI ++ )
        {
            Layer layer = new Layer ( );
            layers.add ( layer ); 
            
            for ( int lI = 0; lI <= structure.get ( lSI ); lI ++ )
            {
                //Neuron ( double eta, double alpha, int neuronId, int numberOfWeightsFromNextNeuron )\
                int numberOfWeightsFromNextNeuron = lSI + 1 < structure.size ( ) ? structure.get ( lSI + 1 ) : 0;
                
                Neuron newNeuron = new Neuron ( eta, alpha, lI, numberOfWeightsFromNextNeuron );
                
                layer.add ( newNeuron );
                
                //Neuron biasNeuron = layers.get ( layers.get ( lSI ).size ( ) - 1 ).get ( ;
                layers.get ( lSI ).get ( layers.get ( lSI ).size ( ) - 1 ).setOutcome ( 1.0 );
            }
        }
    }
    
    //methods
    public void doForwardPropagation ( int [ ] inputs )
    {
        for ( int iI = 0; iI < inputs.length; iI ++ )
            layers.get ( 0 ).get ( iI ).setOutcome ( inputs [ iI ] );
            
        for ( int lSI = 1; lSI < structure.size ( ); lSI ++ )
        {
            Layer priorLayer = layers.get ( lSI - 1 );
            
            for ( int lI = 0; lI < structure.get ( lSI ); lI ++ )
                layers.get ( lSI ).get ( lI ).doForwardPropagation ( priorLayer );
        }
    }
    

    public void doBackwardPropagation ( int target )
    {
        //calculate output gradients
            //output neuron
        Neuron outcomeNeuron = layers.get ( layers.size ( ) - 1 ).get ( 0 );
        outcomeNeuron.calculateOutcomeGradients ( target );
        
        //calculate hidden gradients
        for ( int lSI = structure.size ( ) - 2; lSI > 0; lSI -- )
        {
            Layer currentLayer = layers.get ( lSI );
            Layer nextLayer = layers.get ( lSI + 1 );
           
            for ( int lI = 0; lI < currentLayer.size ( ); lI ++ )
                currentLayer.get ( lI ).calculateHiddenGradients ( nextLayer );
        }
        
        
        //calculate update weights
        for ( int lSI = structure.size ( ) - 1; lSI > 0; lSI -- )
        {
            Layer currentLayer = layers.get ( lSI );
            Layer priorLayer = layers.get ( lSI - 1 );
           
            for ( int lI = 0; lI < currentLayer.size ( ) - 1; lI ++ )
                currentLayer.get ( lI ).updateWeights ( priorLayer );
        }
    }
    
    
    public double getOutcome ( )
    {
        Neuron outcomeNeuron = layers.get ( layers.size ( ) - 1 ).get ( 0 );
        return outcomeNeuron.getOutcome ( );
    }
}
