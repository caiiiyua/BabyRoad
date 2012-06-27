package com.since1985i.babyroad.exception;


public class DiaryException extends Exception {

	private static final long serialVersionUID = 2958483680413896474L;

    public static final int UNSPECIFIED_EXCEPTION = 0;
    
    //Content has saved
    public static final int CONTENT_HAS_SAVED = 10;
    public static final int CONTENT_HAS_NOT_SAVED = 11;

    private final int mExceptionType;
    
    public DiaryException(String message) {
        super(message);
        mExceptionType = UNSPECIFIED_EXCEPTION;
    }

    public DiaryException(String message, Throwable throwable) {
        super(message, throwable);
        mExceptionType = UNSPECIFIED_EXCEPTION;
    }

    public DiaryException(int exceptionType) {
        super();
        mExceptionType = exceptionType;
    }

    public DiaryException(int exceptionType, String message) {
        super(message);
        mExceptionType = exceptionType;
    }
    
    /**
     * Return the exception type.  Will be OTHER_EXCEPTION if not explicitly set.
     * 
     * @return Returns the exception type.
     */
    public int getExceptionType() {
        return mExceptionType;
    }

}
