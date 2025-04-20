// Create a Runnable task for threading
        Runnable fetchPPAData = () -> {
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT s.state_name, ppa.tariff, ppa.escalation_rate, ppa.contract_duration " +
                     "FROM PowerPurchaseAgreements ppa " +
                     "JOIN StateSubsidies s ON ppa.state_id = s.state_id " +
                     "WHERE s.state_name = ?")) {

                pstmt.setString(1, stateName);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No PPA data found for " + stateName);
                    return;
                }
