export const validateEmail = (email: string): boolean => {
    return email.endsWith('@gmail.com');
  };
  
  export const validatePassword = (password: string): boolean => {
    const hasLetters = /[a-zA-Z]/.test(password);
    const hasNumbers = /\d/.test(password);
    return (
      password.length >= 8 &&
      password.length <= 20 &&
      hasLetters &&
      hasNumbers
    );
  };
  