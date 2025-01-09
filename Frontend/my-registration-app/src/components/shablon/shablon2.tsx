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
          <h3>Личная информация</h3>
          <p>Возраст: {data.age}, Пол: {data.gender === "male" ? "Мужской" : data.gender === "female" ? "Женский" : "Другой"}</p>
        </div>
        <div className="resume-section2">
          <h3>Контакты</h3>
          <p>{data.contacts}</p>
        </div>

        <div className="resume-section2">
          <h3>Опыт работы</h3>
          <p>{data.workExperience}</p>
        </div>

        <div className="resume-section2">
          <h3>Образование</h3>
          <p>{data.education}</p>
        </div>

        <div className="resume-section2">
          <h3>Навыки и награды</h3>
          <p>{data.skillsAndAwards}</p>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate2;
