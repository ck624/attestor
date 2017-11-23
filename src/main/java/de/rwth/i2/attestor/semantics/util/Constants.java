package de.rwth.i2.attestor.semantics.util;

public final class Constants {

    public static final String NULL = "null";
    public static final String ONE = "1";
    public static final String ZERO = "0";
    public static final String MINUS_ONE = "-1";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    
    public static final String INT = "int";
    public static final String INT_0 = "int_0";
    public static final String INT_1 = "int_1";
    public static final String INT_M1 = "int_-1";
    public static final String BOOLEAN = "boolean";
    public static final String NULL_TYPE = "NULL";

    public static boolean isConstant(String name) {

        switch(name) {
            case NULL:
            case ONE:
            case ZERO:
            case MINUS_ONE:
            case TRUE:
            case FALSE:
                return true;
            default:
                return false;
        }
    }
    
    public static boolean isConstantType( String name ) {
    	
    	switch( name ) {
    		case INT:
    		case INT_0:
    		case INT_1:	
    		case INT_M1:
    		case BOOLEAN:
    		case NULL_TYPE:
    			return true;
    		default:
    			return false;
    	}
    	
    }
}
