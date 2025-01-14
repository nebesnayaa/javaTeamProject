import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
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
import { ResumeData } from './components/create-resume/ResumeInterface';

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const handleLogout = async () => { // Логика выхода
    try {
      await axios.post("http://localhost:8080/users/logout", {}, { withCredentials: true });
      window.location.href = "/login";
      setIsAuthenticated(false); 
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  const handleLoginSuccess = () => {
    setIsAuthenticated(true); // Логика успешного входа
  };

  const data: ResumeData = {
    fullName: "Oleksandr Kovalenko",
    position: "Software Developer",
    objective: "To develop programming skills and create innovative solutions that benefit the company and its clients.",
    education: `
      National Technical University of Ukraine "Kyiv Polytechnic Institute" 
      (2016–2020)
      Bachelor of Computer Science
    `,
    workExperience: `
      Junior Software Developer, SoftServe (2021–2023)
      - Developed REST APIs for web applications using Node.js and TypeScript.
      - Integrated with AWS cloud services (S3, Lambda).
      - Participated in frontend development using React.

      Intern Software Developer, EPAM Systems (2020–2021)
      - Wrote tests for existing applications.
      - Contributed to microservices architecture development.
    `,
    skillsAndAwards: `
      - Proficient in programming languages: JavaScript, TypeScript, Java.
      - Databases: MySQL, PostgreSQL.
      - Frameworks: React, Express.js, Spring Boot.
      - Awarded "Best Rookie of the Year" at SoftServe, 2022.
    `,
    languages: "Ukrainian (native), English (fluent), German (intermediate)",
    recommendations: `
      Maria Ivanchenko, CTO at SoftServe.
      Phone: +380 50 123 4567
      Email: maria.ivanchenko@softserve.ua
    `,
    hobbiesAndInterests: `
      - Dancing: 2 years of experience (hip-hop, contemporary).
      - Reading personal development books.
      - Playing the guitar.
    `
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
          <Route path="/profile" element={<Profile onLogout={handleLogout}/>} /> 

          <Route path="/template1" element={<ResumeTemplate1 data={data}/>} />
          <Route path="/template2" element={<ResumeTemplate2 data={data} />} /> 
          <Route path="/template3" element={<ResumeTemplate3 data={data} />} /> 
        </Routes> 
      </BrowserRouter>
    </ContextProvider>
  );
};

export default App;
