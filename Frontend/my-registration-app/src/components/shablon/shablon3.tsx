import React from "react";
import "./ResumeTemplate.css";

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

const ResumeTemplate: React.FC<{ data: ResumeData }> = ({ data }) => {
  return (
    <div className="resume-container">
     <div className="upper-line"></div>
      <div className="resume-header">
        <h1 className="resume-name">{data.fullName}</h1>
        <p className="resume-age-gender">
          Возраст: {data.age}, Пол: {data.gender === "male" ? "Мужской" : data.gender === "female" ? "Женский" : "Другой"}
        </p>
      </div>

      <div className="resume-content">
        <div className="resume-left">
          <div className="resume-section">
            <h3>КОНТАКТЫ</h3>
            <p>{data.contacts}</p>
          </div>

          <div className="resume-section">
            <h3>НАВЫКИ И ДОСТИЖЕНИЯ</h3>
            <p>{data.skillsAndAwards}</p>
          </div>
        </div>

        <div className="resume-right">
          <div className="resume-section">
            <h3>ОПЫТ РАБОТЫ</h3>
            <p>{data.workExperience}</p>
          </div>

          <div className="resume-section">
            <h3>ОБРАЗОВАНИЕ</h3>
            <p>{data.education}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate;
