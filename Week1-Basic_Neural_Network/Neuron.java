import java.util.ArrayList;
import java.util.Random;

public class Neuron
{
    //features
    private double eta;
    private double alpha; 
    private double outcome;
    private double gradient;
    private int numberOfWeightsFromNextNeuron;
    private int neuronId;
    private ArrayList <Synapse> weights;
    
    
    //constructor
    public Neuron ( double eta, double alpha, int neuronId, int numberOfWeightsFromNextNeuron )
    {
        this.eta = eta;
        this.alpha = alpha;
        this.neuronId = neuronId;
        this.numberOfWeightsFromNextNeuron = numberOfWeightsFromNextNeuron;
        gradient = 0.0;
        
        weights = new ArrayList <Synapse> ( );
        
        //initialize weights
        for ( int wI = 0; wI < numberOfWeightsFromNextNeuron; wI ++ )
        {
            weights.add ( new Synapse ( ) );
            weights.get ( wI ).setWeight ( new Random ( ).nextDouble ( ) );
        }
    }
    
    //mehods
    public double getOutcome ( )
    {
        return outcome;
    }
    public double getGradient ( )
    {
        return gradient;
    }
    public ArrayList <Synapse> getWeights ( )
    {
        return weights;
    }
    //tanh(value)
    public double getActivation ( double value )
    {
        return Math.tanh ( value );
    }
    //1 - tanh(value)^2
    public double getPrimeActivation ( double value )
    {
        return 1 - Math.pow ( Math.tanh ( value ), 2 );
    }
    //get distributed weight sigma
    //product sum of current neuron weights * the next layer's gradients
    public double getDistributedWeightSigma ( Layer nextLayer )
    {
        double sigma = 0;
        
        for ( int nLI = 0; nLI < nextLayer.size ( ) - 1; nLI ++ )
            sigma += getWeights ( ).get ( nLI ).getWeight ( ) * nextLayer.get ( nLI ).getGradient ( );
        
        return sigma;
    }
    
    
    //methods mutators/changers
    public void setGradient ( double value )
    {
        gradient = value;
    }
    public void setOutcome ( double value )
    {
        outcome = value;
    }
    public void calculateHiddenGradients ( Layer nextLayer )
    {
        double delta = getDistributedWeightSigma ( nextLayer );
        
        setGradient ( getPrimeActivation ( outcome ) * delta );
    }
    public void calculateOutcomeGradients ( int target )
    {
        double delta = target - outcome;
        
        setGradient ( getPrimeActivation ( outcome ) * delta );
    }
    
    //sigma product of weights and outcomes of prior layer
    public void doForwardPropagation ( Layer priorLayer )
    {
        double sigma = 0;
        
        for ( int pLI = 0; pLI < priorLayer.size ( ); pLI ++ )
            sigma += priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getWeight ( ) * priorLayer.get ( pLI ).getOutcome ( );
            
        setOutcome ( getActivation ( sigma ) );
    }
    
    //newDeltaWeight = eta * thisGradient * priorLayer outcome + alpha priorDeltaWeight
    public void updateWeights ( Layer priorLayer )
    {
        for ( int pLI = 0; pLI < priorLayer.size ( ); pLI ++ )
        {
            double priorDeltaWeight = priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getDeltaWeight ( );
            
            double newDeltaWeight = ( eta * gradient * priorLayer.get ( pLI ).getOutcome ( ) ) + ( alpha * priorDeltaWeight );
            
            priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).setDeltaWeight ( newDeltaWeight );
            priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).setWeight ( priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getWeight ( ) + newDeltaWeight );
        }
    }
}
