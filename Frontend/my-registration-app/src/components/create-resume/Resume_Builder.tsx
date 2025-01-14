import React, { useContext, useState } from 'react';
import "../Style/ResumeStyle.css";
import { ResumeData } from './ResumeInterface';
import { AppContext } from "../../context";
import { Link } from 'react-router-dom';


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
  const [template, setTemplate] = useState<number>(1);

  const context = useContext(AppContext);
  const userId = context?.userId;
  if (!userId) {
    console.error("User ID not found in context");
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleTemplateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setTemplate(Number(e.target.value));
  };


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = { ...formData, userId, template };
    try {
      const response = await fetch('http://localhost:8080/resumes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error('Error:', errorData);
        alert(`Error: ${errorData.message}`);
      } else {
        const responseData = await response.json();
        console.log('Resume created successfully:', responseData);
        alert('Resume created successfully!');
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      alert('Failed to submit resume.');
    }
  };

  return (
    <form className="resume-form" onSubmit={handleSubmit}>
      <h2 className="title">Create resume</h2>
      <div className="form-group">
        <label>Review available templates:</label>
        <div className='templates-container'>
          <Link to="/template1" className='btn-template'>Template 1</Link>
          <Link to="/template2" className='btn-template'>Template 2</Link>
          <Link to="/template3" className='btn-template'>Template 3</Link>
        </div>
      </div>
      <div className="form-group">
        <label>Choose the template:</label>
        <select value={template} onChange={handleTemplateChange}>
          <option value={1}>Template 1</option>
          <option value={2}>Template 2</option>
          <option value={3}>Template 3</option>
        </select>
      </div>
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
        <textarea name="languages" value={formData.languages} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Recommendations:</label>
        <textarea name="recommendations" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Hobbies and interests:</label>
        <textarea name="hobbiesAndInterests" value={formData.hobbiesAndInterests} onChange={handleChange}></textarea>
      </div>
      
      <button type="submit" className="btn-save-resume">Save</button>
    </form>
  );
};

export default ResumeForm;
