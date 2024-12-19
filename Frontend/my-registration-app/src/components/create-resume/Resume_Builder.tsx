import React, { useState } from 'react';
import "../Style/ResumeStyle.css";
import { ResumeData } from './ResumeInterface';


const ResumeForm: React.FC = () => {
  const [formData, setFormData] = useState<ResumeData>({
    fullName: '',
    age: 0,
    gender: '',
    contacts: '',
    objective: '',
    education: '',
    workExperience: '',
    skillsAndAwards: '',
    personalInfo: '',
    recommendations: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'age' ? Number(value) : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Submitted Data:', formData);
  };

  return (
    <form className="resume-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label>ФИО:</label>
        <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Возраст:</label>
        <input type="number" name="age" value={formData.age} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Пол:</label>
        <select name="gender" value={formData.gender} onChange={handleChange}>
          <option value="">Выберите</option>
          <option value="male">Мужской</option>
          <option value="female">Женский</option>
          <option value="other">Другой</option>
        </select>
      </div>
      <div className="form-group">
        <label>Контакты:</label>
        <input type="text" name="contacts" value={formData.contacts} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Цель:</label>
        <textarea name="objective" value={formData.objective} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Образование:</label>
        <textarea name="education" value={formData.education} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Опыт работы:</label>
        <textarea name="workExperience" value={formData.workExperience} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Навыки и награды:</label>
        <textarea name="skillsAndAwards" value={formData.skillsAndAwards} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Личная информация и хобби:</label>
        <textarea name="personalInfo" value={formData.personalInfo} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Рекомендации:</label>
        <textarea name="recommendations" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <button type="submit" className="submit-button">Сохранить</button>
    </form>
  );
};

export default ResumeForm;
