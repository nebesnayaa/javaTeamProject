import React, { useContext, useState } from 'react';
import "./Style/SignInRegister.css";
import { validateEmail, validatePassword } from '../validation';
import { AppContext } from '../context';
import { useNavigate } from 'react-router-dom';

interface LoginFormProps {
  onSuccess: () => void;
} 

const LoginForm: React.FC<LoginFormProps> = ({ onSuccess }) => {
  const [email, setEmail] = useState<string>(''); 
  const [password, setPassword] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');

  const context = useContext(AppContext);

  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateEmail(email)) {
      alert('Email must not contain @mail.ru and must be valid.');
      return;
    }
    if (!validatePassword(password)) {
      alert('Password must contain at least 6 symbols.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
        credentials: 'include'
      });

      if(response.ok) {
        const data = await response.json();
        if(context){
          context.setUserId(data.id);
          console.log("UserId was set to the context: " + context.userId);
        }
        setErrorMessage('');
        alert("Welcome, " + data.username);
        onSuccess();
        navigate("/profile");
      } else{
        const errorData = await response.json();
        setErrorMessage(errorData.message || 'Failed to log in. Please try again.');
      }
      
    } catch (error) {
      setErrorMessage('An error occurred. Please try again later.');
    }
  };

  return (
    <div className="wrapper">
      <div className="login-container">
        <h2>Sing-in</h2>
        
        <form onSubmit={handleLogin} className="login-form">
          <div className="login-form">
            <div className="input-wrapper">
              <div className="icon-container">
                <svg className="icon" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#D9D9D9">
                  <path d="M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-160v-112q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v112H160Zm80-80h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z"/>
                </svg>
              </div>
              <input
                type="email"
                id="email"
                name="email"
                placeholder="Input email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="input-wrapper">
              <div className="icon-container">
                <svg className="icon" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#D9D9D9">
                  <path d="M240-80q-33 0-56.5-23.5T160-160v-400q0-33 23.5-56.5T240-640h40v-80q0-83 58.5-141.5T480-920q83 0 141.5 58.5T680-720v80h40q33 0 56.5 23.5T800-560v400q0 33-23.5 56.5T720-80H240Zm0-80h480v-400H240v400Zm240-120q33 0 56.5-23.5T560-360q0-33-23.5-56.5T480-440q-33 0-56.5 23.5T400-360q0 33 23.5 56.5T480-280ZM360-640h240v-80q0-50-35-85t-85-35q-50 0-85 35t-35 85v80ZM240-160v-400 400Z"/>
                </svg>
              </div>
              <input
                type="password"
                id="password"
                name="password"
                placeholder="Input password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            
            {errorMessage && <p className="login-message error">{errorMessage}</p>}

            <input type="submit" value="Sign in" />
          </div>
        </form>
        
      </div>
    </div>
  );
};

export default LoginForm;
