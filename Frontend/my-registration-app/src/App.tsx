import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useContext, useState } from "react";
import axios from "axios";
import Navbar from "./components/Navbar";
import Main from "./components/Main";
import RegistrationForm from "./components/Registration";
import Login from "./components/Login";
import Profile from "./components/Profile";
import ResumeForm from './components/create-resume/Resume_Builder';
import UserList from "./components/UserList";
import ResumeTemplate1 from "./components/templates/template1";
import ResumeTemplate2 from "./components/templates/template2";
import ResumeTemplate3 from "./components/templates/template3";
import { ContextProvider } from "./context";
import EditResume from "./components/create-resume/Edit_Resume";
import { AppContext } from "./context";


const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const context = useContext(AppContext);

  const handleLogout = async () => { // Логика выхода
    try {
      await axios.post("http://localhost:8080/users/logout", {}, { withCredentials: true });
      window.location.href = "/login";
      context?.setUserId("");
      setIsAuthenticated(false);
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  const handleLoginSuccess = () => {
    setIsAuthenticated(true); // Логика успешного входа
  };

  return (
    <ContextProvider>
      <BrowserRouter>
        <Navbar isAuthenticated={isAuthenticated} />
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/signup" element={<RegistrationForm onSuccess={handleLoginSuccess} />} />
          <Route path="/login" element={<Login onSuccess={handleLoginSuccess} />} />
          
          <Route path="/Users" element={<UserList />} />
          <Route path="/create-resume" element={<ResumeForm />} />
          <Route path="/edit-resume" element={<EditResume />} />
          <Route path="/profile" element={<Profile onLogout={handleLogout} />} />

          <Route path="/template1" element={<ResumeTemplate1 />} />
          <Route path="/template2" element={<ResumeTemplate2 />} />
          <Route path="/template3" element={<ResumeTemplate3 />} />

        </Routes>
      </BrowserRouter>
    </ContextProvider>
  );
};

export default App;
