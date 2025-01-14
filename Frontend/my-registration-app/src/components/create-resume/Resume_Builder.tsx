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
      <h2 className="title">Creating a resume</h2>
      <div className="form-group">
        <label>Review available templates:</label>
        <div className='templates-container'>
          <Link to="/template1" className='btn-template'>Template 1</Link>
          <Link to="/template2" className='btn-template'>Template 2</Link>
          <Link to="/template3" className='btn-template'>Template 3</Link>
        </div>
      </div>
      <div className="form-group">
        <label>Age:</label>
        <input type="number" name="age" value={formData.age} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Full name:</label>
        <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Contacts:</label>
        <input type="text" name="contacts" value={formData.contacts} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Purpose:</label>
        <textarea name="objective" value={formData.objective} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Education:</label>
        <textarea name="education" value={formData.education} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Work experience:</label>
        <textarea name="workExperience" value={formData.workExperience} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Skills and awards:</label>
        <textarea name="skillsAndAwards" value={formData.skillsAndAwards} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Languages:</label>
        <textarea name="languages" value={formData.languages} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Recomendation:</label>
        <textarea name="recommendations" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <div className="btn-box">
        <button type="submit" className="submit-button">Save</button>
      </div>
    </form>
  );
};

export default ResumeForm;
