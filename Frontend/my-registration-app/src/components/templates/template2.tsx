import React from "react";
import "./ResumeTemplate2.css";
import { ResumeData } from '../create-resume/ResumeInterface';

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

const ResumeTemplate2: React.FC<{ data: ResumeData }> = ({ data }) => {
  return (
    <div className="resume-container2">
      <div className="resume-header2">
        <h1 className="resume-name2">{data.fullName}</h1>
      </div>
      <div className="resume-content2">
        <div className="resume-section2">
          <h3>Position</h3>
          <p>{data.position}</p>
        </div>

        <div className="resume-section2">
          <h3>Personal information</h3>
          <p>Age: {personalData.age}, Gender: {personalData.gender === "male" ? "Male" : personalData.gender === "female" ? "Female" : "Other"}</p>
        </div>

        <div className="resume-section2">
          <h3>Contacts</h3>
          <p>Email: {personalData.email} Phone: {personalData.phone}</p>
        </div>

        <div className="resume-section2">
          <h3>Work experience</h3>
          <p>{data.workExperience}</p>
        </div>

        <div className="resume-section2">
          <h3>Education</h3>
          <p>{data.education}</p>
        </div>

        <div className="resume-section2">
          <h3>Skills and awards</h3>
          <p>{data.skillsAndAwards}</p>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate2;
