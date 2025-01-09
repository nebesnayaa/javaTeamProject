import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import RegistrationForm from "./components/Registration";
import UserList from "./components/UserList";
import Main from "./components/Main";
import ResumeForm from './components/create-resume/Resume_Builder';
import Profile from "./components/Profile";
import ResumeTemplate1 from "./components/shablon/shablon1";
import ResumeTemplate2 from "./components/shablon/shablon2";

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const handleLogout = () => {
    setIsAuthenticated(false); // Логика выхода
  };

  const handleLoginSuccess = () => {
    setIsAuthenticated(true); // Логика успешного входа
  };
  interface ResumeData {
    fullName: string;
    age: number;
    gender: string;
    contacts: string;
    objective: string;
    education: string;
    workExperience: string;
    skillsAndAwards: string;
  }
  const data: ResumeData = {
    fullName: "John Doe",
    age: 29,
    gender: "Male",
    contacts: "johndoe@example.com | +1 (555) 123-4567",
    objective: "To secure a challenging position as a Software Engineer, where I can utilize my skills in web development and problem-solving.",
    education: "Bachelor of Science in Computer Science, University of Technology, 2016-2020",
    workExperience: 
      "Software Engineer, TechCorp Inc., 2020-Present\n" +
      "- Developed and maintained scalable web applications using React and Node.js.\n" +
      "- Led a team of 5 developers to deliver a customer-facing portal, increasing customer satisfaction by 15%.\n" +
      "- Implemented CI/CD pipelines to streamline deployments and reduce errors.",
    skillsAndAwards: 
      "Skills:\n" +
      "- Proficient in JavaScript, TypeScript, and Python.\n" +
      "- Experienced in React, Node.js, and MongoDB.\n" +
      "- Strong knowledge of RESTful APIs and microservices architecture.\n\n" +
      "Awards:\n" +
      "- Employee of the Month, TechCorp Inc., March 2022.\n" +
      "- Winner, Hackathon 2021: Built an AI-powered chatbot."
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
          <Route path="/profile" element={<Profile />} /> 
          <Route path="/shablon1" element={<ResumeTemplate1 data={data} />} />
          <Route path="/shablon2" element={<ResumeTemplate2 data={data} />} /> 
        </Routes> 
      </BrowserRouter>
    </>
  );
};

export default App;
