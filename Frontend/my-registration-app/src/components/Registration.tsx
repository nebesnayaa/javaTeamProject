import React, { useState } from 'react';
import './Style/RegistrationForm.css';
import { validateEmail, validatePassword } from '../validation';

interface RegistrationFormProps {
  onSuccess: () => void;
}

const RegistrationForm: React.FC<RegistrationFormProps> = ({ onSuccess }) => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [username, setUsername] = useState<string>('');
  const [message, setMessage] = useState<string>('');

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();

    const isEmailValid = validateEmail(email);
    const isPasswordValid = validatePassword(password);

    if (!isEmailValid) {
      alert('Email должен содержать @gmail.com');
      return;
    }

    if (!isPasswordValid) {
      alert('Пароль должен быть от 8 до 20 символов и содержать буквы и цифры.');
      return;
    }

    const newUser = { username, email, password };
    setMessage(`Привет, ${newUser.username}!`);
    onSuccess(); // Вызываем onSuccess после успешной регистрации
  };

  return (
    <div className="registration-container">
      <form onSubmit={handleRegister} className="registration-form">
        <label className="registration-label">
          Имя пользователя:
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="registration-input"
            required
          />
        </label>
        <label className="registration-label">
          Введите почту:
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="registration-input"
            required
          />
        </label>
        <label className="registration-label">
          Введите пароль:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="registration-input"
            required
          />
        </label>
        <button type="submit" className="registration-button">Зарегистрироваться</button>
      </form>
      {message && <p className="registration-message">{message}</p>}
    </div>
  );
};

export default RegistrationForm;
