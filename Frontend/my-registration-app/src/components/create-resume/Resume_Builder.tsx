import React, { useState } from 'react';
import "../Style/ResumeStyle.css";
import { ResumeData } from './ResumeInterface';


const ResumeForm: React.FC = () => {
  const [formData, setFormData] = useState<ResumeData>({
    fullName: '',
    position: '',
    objective: '',
    education: '',
    workExperience: '',
    skillsAndAwards: '',
    languages: '',
    recommendations: '',
    hobbiesAndInterests: ''
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
      <h2 className="title">Create resume</h2>
      <div className="form-group">
        <label>Full name:</label>
        <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Position:</label>
        <input type="text" name="position" value={formData.position} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Aim:</label>
        <textarea name="objective" value={formData.objective} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Education:</label>
        <textarea name="education" value={formData.education} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Working experience:</label>
        <textarea name="workExperience" value={formData.workExperience} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Skills and awards:</label>
        <textarea name="skillsAndAwards" value={formData.skillsAndAwards} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Languages:</label>
        <textarea name="languages" value={formData.skillsAndAwards} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Recommendations:</label>
        <textarea name="recommendations" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Hobbies and interests:</label>
        <textarea name="hobbiesAndInterests" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <div className="btn-box">
        <button type="submit" className="submit-button">Save</button>
      </div>
    </form>
  );
};

export default ResumeForm;
