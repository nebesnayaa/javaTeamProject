export const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email) && !email.includes('@ru');
  };
  
  export const validatePassword = (password: string): boolean => {
    return password.length >= 8 && password.length <= 20 && /\d/.test(password) && /[A-Za-z]/.test(password);
  };
  