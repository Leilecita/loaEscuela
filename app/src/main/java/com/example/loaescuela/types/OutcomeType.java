package com.example.loaescuela.types;

public enum OutcomeType {

        ALL(Constants.TYPE_ALL),
        SUELDO(Constants.OUTCOME_SALARY),
        EXTRACCION(Constants.OUTCOME_EXTRACTION),
        DEPOSITO(Constants.OUTCOME_DEPOSIT),
        SANTI(Constants.OUTCOME_SANTI);

        private final String name;

        OutcomeType(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }


}
