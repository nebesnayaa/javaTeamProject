import React, { useState } from 'react';
import './Style/Login.css';
import { validateEmail, validatePassword } from '../validation';

interface LoginFormProps {
  onSuccess: () => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onSuccess }) => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [message, setMessage] = useState<string>('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();

    const isEmailValid = validateEmail(email);
    const isPasswordValid = validatePassword(password);

    if (!isEmailValid) {
      alert('Email не должен содержать @mail.ru и должен быть действительным.');
      return;
    }

    if (!isPasswordValid) {
      alert('Пароль должен быть минимум 6 символов.');
      return;
    }

    setMessage(`Добро пожаловать, ${email}!`);
    onSuccess(); // Вызываем onSuccess после успешного входа
  };

  return (
    <div className="login-container">
    <form onSubmit={handleLogin} className="login-form">
      <label className="login-label">
        Введите почту:
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="login-input"
          required
        />
      </label>
      <label className="login-label">
        Введите пароль:
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="login-input"
          required
        />
      </label>
      <button type="submit" className="login-button">Войти</button>
    </form>
    {message && <p className="login-message">{message}</p>}
  </div>
  
  );
};

export default LoginForm;
