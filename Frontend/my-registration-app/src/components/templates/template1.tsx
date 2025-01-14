import React, { useContext, useEffect, useState } from "react";
import "./ResumeTemplate1.css";
import { ResumeData } from '../create-resume/ResumeInterface';
import { AppContext } from "../../context";
import axios from "axios";

interface UserData {
  age: string;
  gender: string;
  email: string;
  phone: string;
}

const personalData: UserData = {
  age: "25",
  gender: "Male",
  email: "sashka2000@gmail.com",
  phone: "0987654678"
}

const ResumeTemplate1: React.FC<{ data: ResumeData }> = ({ data }) => {
  // const [userData, setUserData] = useState({
  //   id: "",
  //   email: "",
  //   phone: "",
  //   gender: "",
  //   age: ""
  // });
    
  // const context = useContext(AppContext);
  
  // useEffect(() => {
  //   const userId = context?.userId;
  //   console.log("Session ID from context:", userId);

  //   if (!userId) {
  //     console.error("User ID not found in context");
  //     userData.email = personalData.email;
  //     userData.phone = personalData.phone;
  //     userData.gender = personalData.gender;
  //     userData.age = personalData.age;
  //     return;
  //   }

  //   axios
  //     .get(`http://localhost:8080/users/one/${userId}`, { withCredentials: true })
  //     .then((response) => {
  //       setUserData(response.data);
  //     })
  //     .catch((error) => console.error("Error fetching profile data:", error));
    
  //   axios
  //     .get(`http://localhost:8080/resumes/userId/${userId}`, { withCredentials: true }) // поки що некоректний запит
  //     .then((response) => {
  //       setResumeData(response.data);
  //       setResumeAvailable(true);
  //     })
  //     .catch((error) => {
  //       console.error("No resume found:", error);
  //       setResumeAvailable(false);
  //     });
  // }, []);
  
  return (
    <div className="resume-container1">
      <div className="upper-line1"></div>
      <div className="resume-header1">
        <h1 className="resume-name1">{data.fullName}</h1>
        <p className="resume-age-gender1">
          Age: {personalData.age}, Gender: {personalData.gender === "male" ? "Male" : personalData.gender === "female" ? "Female" : "Other"}
        </p>
      </div>
      <div className="resume-content1">
        <div className="resume-section1">
          <h3>Contacts</h3>
          <p>Email: {personalData.email} Phone: {personalData.phone}</p>
        </div>

        <div className="resume-section1">
          <h3>Position</h3>
          <p>{data.position}</p>
        </div>

        <div className="resume-section1">
          <h3>Aim</h3>
          <p>{data.objective}</p>
        </div>

        <div className="resume-section1">
          <h3>Education</h3>
          <p>{data.education}</p>
        </div>

        <div className="resume-section1">
          <h3>Work experience</h3>
          <p>{data.workExperience}</p>
        </div>

        <div className="resume-section1">
          <h3>Skills and awards</h3>
          <p>{data.skillsAndAwards}</p>
        </div>

        <div className="resume-section1">
          <h3>Languages</h3>
          <p>{data.languages}</p>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate1;
