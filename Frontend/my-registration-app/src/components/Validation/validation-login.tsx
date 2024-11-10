export const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email) && !email.includes('@ru');
  };
  
  export const validatePassword = (password: string): boolean => {
    return password.length >= 6;
  };
  