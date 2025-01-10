import React from "react";
import "./ResumeTemplate2.css";

export interface ResumeData {
  fullName: string;
  age: number;
  gender: string;
  contacts: string;
  objective: string;
  education: string;
  workExperience: string;
  skillsAndAwards: string;
}

const ResumeTemplate2: React.FC<{ data: ResumeData }> = ({ data }) => {
  return (
    <div className="resume-container">
      <div className="resume-header">
        <h1 className="resume-name">{data.fullName}</h1>
      </div>
      <div className="resume-content">
      <div className="resume-section2">
          <h3>Personal information</h3>
          <p>Age: {data.age}, Gender: {data.gender === "male" ? "Male" : data.gender === "female" ? "Female" : "Other"}</p>
        </div>
        <div className="resume-section2">
          <h3>Contacts</h3>
          <p>{data.contacts}</p>
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
