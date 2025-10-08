/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package rules;

/**
 * RulesOf6005 represents the collaboration policy of 6.005 as described by the
 * general information on Stellar.
 */
public class RulesOf6005 {

    /**
     * Judge whether a given piece of code may be used in an assignment (problem
     * set or team project) or not, according to the 6.005 collaboration policy.
     * 
     * @param writtenByYourself      true if the code in question was written by
     *                               yourself or, in the case of a team project,
     *                               your teammates,
     *                               otherwise false.
     * @param availableToOthers      if not writtenByYourself, whether or not the
     *                               code in question is available to all other
     *                               students in the class.
     *                               Otherwise ignored.
     * @param writtenAsCourseWork    if not writtenByYourself, whether or not the
     *                               code in question was written specifically as
     *                               part of a solution to
     *                               a 6.005 assignment, in the current or past
     *                               semesters. Otherwise
     *                               ignored.
     * @param citingYourSource       if not writtenByYourself, whether or not you
     *                               properly cite your source. Otherwise ignored.
     * @param implementationRequired whether the assignment specifically asks
     *                               you to implement the feature in question.
     * @return Whether or not, based on the information provided in the
     *         arguments, you are likely to be allowed to use the code in
     *         question in your assignment, according to the 6.005 collaboration
     *         policy for the current semester.
     */
    public static boolean mayUseCodeInAssignment(boolean writtenByYourself,
            boolean availableToOthers, boolean writtenAsCourseWork,
            boolean citingYourSource, boolean implementationRequired) {

        // If the assignment specifically requires you to implement it yourself,
        // you cannot use code written by others
        if (implementationRequired && !writtenByYourself) {
            return false;
        }

        // If you wrote the code yourself, you can use it
        if (writtenByYourself) {
            return true;
        }

        // For code not written by yourself:
        // 1. It must be available to others (publicly available)
        // 2. It must NOT be written as coursework for 6.005
        // 3. You must cite your source
        return availableToOthers && !writtenAsCourseWork && citingYourSource;
    }

    /**
     * Main method of the class.
     * 
     * Runs the mayUseCodeInAssignment method.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("You may certainly use code you wrote yourself: " +
                RulesOf6005.mayUseCodeInAssignment(true, false, true, true, true));
    }
}
