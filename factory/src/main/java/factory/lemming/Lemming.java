package factory.lemming;

/**
 * 
 * @author George Haddad
 *
 */
public class Lemming {

        private Ability ability = null;
        private String hairColor = null;
        private Integer age = null;
        private String id = null;
        
        public Lemming() {
                
        }
        
        public Lemming(String id, Integer age, String hairColor, Ability ability) {
                this.id = id;
                this.age = age;
                this.hairColor = hairColor;
                this.ability = ability;
        }
        
        public final Ability getAbility() {
                return ability;
        }
        
        public final void setAbility(Ability ability) {
                this.ability = ability;
        }
        
        public final String getHairColor() {
                return hairColor;
        }
        
        public final void setHairColor(String hairColor) {
                this.hairColor = hairColor;
        }

        public final Integer getAge() {
                return age;
        }

        public final void setAge(Integer age) {
                this.age = age;
        }

        public final String getId() {
                return id;
        }
        
        public final void setId(String id) {
                this.id = id;
        }
}
