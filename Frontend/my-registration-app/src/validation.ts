export const validateEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!emailRegex.test(email)) {
    console.log("Invalid");
    return false; // Якщо email не відповідає загальному формату, він одразу вважається неправильним
  }

  const bannedDomains = ['ru'];
  const domain = email.split('@')[1];
  const isBannedDomain = bannedDomains.some(
    (banned) => domain === banned || domain.endsWith(`.${banned}`)
  );
  return !isBannedDomain; // Email правильний, якщо він не має забороненого домену
};
  
export const validatePassword = (password: string): boolean => {
  const hasLetters = /[a-zA-Z]/.test(password);
  const hasNumbers = /\d/.test(password);
  return (
    password.length >= 6 &&
    password.length <= 20 &&
    hasLetters &&
    hasNumbers
  );
};
  