// Validate user input
        try {
            validateState(stateName);
        } catch (InvalidStateException e) {
            System.err.println("" + e.getMessage());
            return; // stop execution if invalid
        }


// Validate against known states
    private static void validateState(String state) throws InvalidStateException {
        if (!VALID_STATES.contains(state)) {
            throw new InvalidStateException(
                "Invalid state or union territory entered.\n" +
                "Valid entries include: " + String.join(", ", VALID_STATES)
            );
        }
    }
}
