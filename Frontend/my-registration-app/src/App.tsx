import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import RegistrationForm from "./components/Registration";
import UserList from "./components/UserList";
import Main from "./components/Main";
import ResumeForm from './components/create-resume/Resume_Builder';

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const handleLogout = () => {
    setIsAuthenticated(false); // Логика выхода
  };

  const handleLoginSuccess = () => {
    setIsAuthenticated(true); // Логика успешного входа
  };

  return (
    <>
      <BrowserRouter>
        <Navbar isAuthenticated={isAuthenticated} onLogout={handleLogout} />
        <Routes>
          <Route path="/" element={<Main/>} />
          <Route path="/Register" element={<RegistrationForm onSuccess={handleLoginSuccess} />} />
          <Route path="/Login" element={<Login onSuccess={handleLoginSuccess} />} />
          <Route path="/Users" element={<UserList/>} />
          <Route path="/resume" element={<ResumeForm />} /> 
        </Routes> 
      </BrowserRouter>
    </>
  );
};

export default App;
