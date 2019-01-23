package factory;

import java.util.Random;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import factory.lemming.Ability;
import factory.lemming.Lemming;

/**
 * 
 * @author George Haddad
 *
 */
public class LemmingGenerator {

    private final static Random RAND = new Random();
    private final static RandomBasedGenerator ID_GEN = Generators.randomBasedGenerator();
    
    private final static Ability[] ABILITIES = new Ability[] {
           Ability.BASHER,
           Ability.BUILDER,
           Ability.CLIMBER,
           Ability.DIGGER,
           Ability.EXPLODER,
           Ability.PARACHUTER,
           Ability.STOPPER
    };
    
    private static final String[] HAIR_COLORS = new String[] {
            "green",
            "red",
            "blue",
            "yellow",
            "purple"
    };
    
    public LemmingGenerator() {
        
    }
    
    public Lemming[] generate(int size) {
        Lemming[] lemmings = new Lemming[size];
        for(int i=0; i < lemmings.length; i++) {
            Lemming lem = new Lemming();
            lem.setId(ID_GEN.generate().toString());
            lem.setAbility(ABILITIES[RAND.nextInt(ABILITIES.length)]);
            lem.setHairColor(HAIR_COLORS[RAND.nextInt(HAIR_COLORS.length)]);
            lem.setAge(Integer.valueOf(RAND.nextInt(75)));
            lemmings[i] = lem;
        }
        
        return lemmings;
    }
}
