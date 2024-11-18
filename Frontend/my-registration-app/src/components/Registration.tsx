import React, { useState } from "react";
import "./Style/RegistrationForm.css";
import { validateEmail, validatePassword } from "../validation";

interface RegistrationFormProps {
  onSuccess: () => void;
}

const RegistrationForm: React.FC<RegistrationFormProps> = ({ onSuccess }) => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [isAgreed, setIsAgreed] = useState<boolean>(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      setErrorMessage("Пароли не совпадают");
      return;
    }

    if (!validateEmail(email) || !validatePassword(password)) {
      setErrorMessage("Неверный формат email или пароля");
      return;
    }

    // Допустим, регистрация успешна
    onSuccess();
  };

  return (
    <div className="wrapper">
      <div className="registration-container">
        <h2>Sing-in</h2>
        <form onSubmit={handleSubmit}>
          <div className="registration-form">
            <div className="input-wrapper">
              <div className="icon-container">
                <svg className="icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="16" height="16">
                  <use xlinkHref="#man-people-user"></use>
                </svg>
              </div>
              <input
                type="email"
                id="email"
                name="email"
                placeholder="Введите email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="input-wrapper">
              <div className="icon-container">
                <svg className="icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="16" height="16">
                  <use xlinkHref="#lock-locker"></use>
                </svg>
              </div>
              <input
                type="password"
                id="password"
                name="password"
                placeholder="Введите пароль"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <div className="input-wrapper">
              <div className="icon-container">
                <svg className="icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="16" height="16">
                  <use xlinkHref="#lock-locker"></use>
                </svg>
              </div>
              <input
                type="password"
                id="confirm-password"
                name="confirm-password"
                placeholder="Подтвердите пароль"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>

            {errorMessage && <div className="registration-message">{errorMessage}</div>}

            <input type="submit" value="Зарегистрироваться" />
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegistrationForm;
