package Utils.JsonParser.SupportClasses;

import Server.Model.Game.Cards.Card;
import Server.Model.Game.Cards.CardGolden;
import Server.Model.Game.Artifacts;

import java.util.Map;

/**
 * Class used by the Json parser as a middleman for the creation of CardGolden Objects.<br>
 * It extends CardResourceSupportClass and mocks all the attributes in CardGolden.<br>
 * It checks that the inputs provided by the gson deserializer are valid and if so proceeds to create a CardGolden object,
 * otherwise it throws an IllegalArgumentException specifying what is wrong with the inputs provided.
 */
public class CardGoldenSupportClass extends CardResourceSupportClass{
    //ATTRIBUTES
    private boolean requiresCorner;
    private Artifacts requiredArtifact;
    private Map<Artifacts, Integer> constraint;





    //VALIDATION METHODS

    /**
     * Method checks if the provided points do not contradict the definition of a Golden Card:<br>
     * -Can't be a negative value.<br>
     * -Has to be either 1, 2, 3 or 5.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Int value with validated points.
     */
    protected int checkGoldenPoints(){
        int points = super.getPoints();
        if(points < 0)
          throw new IllegalArgumentException("Provided negative points. Golden card points expected to be 1, 2, 3 or 5");

        if(points == 0)
            throw new IllegalArgumentException("Card points are set to 0. Golden card points expected to be 1, 2, 3 or 5");

        if(points == 4)
            throw new IllegalArgumentException("Card points are set to 4. Golden card points expected to be 1, 2, 3 or 5");

        if(points > 5)
            throw new IllegalArgumentException("Provided too many card points. Golden card points expected to be 1, 2, 3 or 5");

        else
            return points;
    }

    /**
     * Method checks if card requires an Artifact as points multiplier.<br>
     * If requiredArtifact is not null then method ensures that:<br>
     * -Points has to be 1.<br>
     * -requiresCorner has to be false<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Artifact enum entry required by card.
     */
    protected Artifacts checkRequiredArtifact(){

        if(requiredArtifact != null && !requiredArtifact.equals(Artifacts.NULL)){
            if(checkGoldenPoints() != 1)
                throw new IllegalArgumentException("Provided points != 1 and requiredArtifact not null.\n" +
                        "Expected points = 1 requiredArtifact not null or points != 1 requiredArtifact null");

            if(requiresCorner)
                throw new IllegalArgumentException("Provided requiresCorner = true and requiredArtifact not null.\n" +
                        "Expected requiresCorner = true requiredArtifact = null or requiredCorner = false requiredArtifact != null");
        }

        return this.requiredArtifact;
    }

    /**
     * Method checks if card requires corners covered as points multiplier.<br>
     * If reuquiresCorner is true then method ensures that:<br>
     * -Points has to be 2.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return boolean value of requiresCorner after checking it is valid.
     */
    protected boolean checkRequiresCorner(){
        if(requiresCorner){
            if(checkGoldenPoints() != 2)
                throw new IllegalArgumentException("Provided points != 2 and requiresCorner = true.\n" +
                        "Expected requiresCorner = true points = 2 or requiredCorner = false points != 2");
        }

        return this.requiresCorner;
    }


    /**
     * Method checks if the constraint provided is a valid constraint.<br>
     * Method ensures that the constraint meets the following requirements:<br>
     * -Constraint can't be empty.<br>
     * -Constraint can't be null.<br>
     * -Constraint entries can't have negative values.<br>
     * -Constraint entries can't have 0 value.<br>
     * -Constraint can only require a maximum total of 5 artifact units.<br>
     * -Constraint has to require 2 different types of resources and 3 units if card requires artifact.<br>
     * -Constraint has to require 2 different types of resources and 4 units if card requires corner.<br>
     * -Constraint has to require 1 type of resource and 3 units if card provides 3 points outright.<br>
     * -Constraint has to require 1 type of resource and 5 units if card provides 5 points outright.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Map containing the constraint after successful validation by the method.
     */
    protected Map<Artifacts, Integer> checkConstraint(){
        //Constraint can't be null.
        if(constraint == null)
            throw new IllegalArgumentException("No constraint has been provided.\n" +
                    "All gold cards must have a constraint.");
        //Constraint can't be empty.
        if(constraint.isEmpty())
            throw new IllegalArgumentException("Provided empty constraint.\n" +
                    "Expected constraint not empty.");

        //Counts total amount of units in the constraint.
        int constraintCounter = 0;

        for(Map.Entry<Artifacts, Integer> entry : constraint.entrySet()){
            //Constraint entries can't have negative values.
            if(entry.getValue() < 0)
                throw new IllegalArgumentException("Provided negative value in constraint map at key:"+entry.getKey()+".\n" +
                        "Expected only values > 0");
            //Constraint entries can't have 0 value.
            if(entry.getValue() == 0)
                throw new IllegalArgumentException("Provided null value in constraint map at key:"+entry.getKey()+".\n" +
                        "Expected only values > 0");

            //Updates amount of units counted in the constraint.
            constraintCounter = constraintCounter + entry.getValue();
        }


        //Constraint can only require a maximum total of 5 artifact units.
        if(constraintCounter > 5)
            throw new IllegalArgumentException("Provided more than 5 units in the constraint.\n" +
                    "Expected a total of 5 units in the constraint.");



        //Constraint has to require 2 different types of resources and 3 units if card requires artifact.
        if(requiredArtifact != null && !requiredArtifact.equals(Artifacts.NULL)){
            if(constraint.size() != 2)
                throw new IllegalArgumentException("Provided requiredArtifact != null and 1 or more than 2 resource types in the constraint.\n" +
                        "Expected requiredArtifact != null and only 2 resource types in the constraint or requiredArtifact = null");
            if(constraintCounter != 3)
                throw new IllegalArgumentException("Provided more or less than 3 units in the constraint.\n" +
                        "Expected if requiredArtifact is not null only 3 units in the constraint.");
        }


        //Constraint has to require 2 different types of resources and 4 units if card requires corner.
        if(requiresCorner){
            if(constraint.size() != 2)
                throw new IllegalArgumentException("Provided requiresCorner = true and 1 or more than 2 resource types in the constraint.\n" +
                        "Expected requiresCorner = true and only 2 resource types in the constraint or requiresCorner = false");
            if(constraintCounter != 4)
                throw new IllegalArgumentException("Provided more or less than 4 units in the constraint.\n" +
                        "Expected if requiredCorner = true only 4 units in the constraint.");
        }


        //Constraint has to require 1 type of resource and 3 units if card provides 3 points outright.
        if(checkGoldenPoints() == 3){
            if(constraint.size() != 1)
                throw new IllegalArgumentException("Provided points = 3 more than 1 resource type in the constraint.\n" +
                        "Expected points = 3 and only 1 resource type in the constraint or points != 3");
            if(constraintCounter != 3)
                throw new IllegalArgumentException("Provided more or less than 3 units in the constraint.\n" +
                        "Expected if points = 3 only 3 units in the constraint.");
        }


        //Constraint has to require 1 type of resource and 5 units if card provides 5 points outright.
        if(checkGoldenPoints() == 5){
            if(constraint.size() != 1)
                throw new IllegalArgumentException("Provided points = 5 and more than 1 resource type in the constraint.\n" +
                        "Expected points = 5 and only 1 resource type in the constraint or points != 5");
            if(constraintCounter != 5)
                throw new IllegalArgumentException("Provided less than 5 units in the constraint.\n" +
                        "Expected if requiredCorner = true only 4 units in the constraint.");
        }

        return this.constraint;
    }





    //CARD CREATOR
    /**
     * Creates new CardGolden using the appropriate constructor and giving it as parameters the values
     * returned by the validation methods.
     * @return CardGolden object cast to Card created with parsed and validated inputs.
     */
    public Card createGoldenCard(){
        if(requiresCorner)
            return new CardGolden(super.checkCardColor(), checkGoldenPoints(), super.checkCorners(), checkRequiresCorner(), checkConstraint());
        if(requiredArtifact != null && !requiredArtifact.equals(Artifacts.NULL))
            return new CardGolden(super.checkCardColor(), checkGoldenPoints(), super.checkCorners(), checkRequiredArtifact(), checkConstraint());
        else
            return new CardGolden(super.checkCardColor(), checkGoldenPoints(), super.checkCorners(), checkConstraint());
    }
}
