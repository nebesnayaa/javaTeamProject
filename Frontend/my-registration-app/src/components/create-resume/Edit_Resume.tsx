import React, { useState, useEffect, useContext } from 'react';
import "../Style/ResumeStyle.css";
import { ResumeData } from './ResumeInterface';
import { AppContext } from "../../context";
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';

const EditResume: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const context = useContext(AppContext);
  const userId = context?.userId;

  const { data } = location.state || {}; // Убедитесь, что state передается при навигации

  const [formData, setFormData] = useState<ResumeData>({
    fullName: '',
    position: '',
    objective: '',
    education: '',
    workExperience: '',
    skillsAndAwards: '',
    languages: '',
    recommendations: '',
    hobbiesAndInterests: '',
  });

  const [id, setId] = useState<string>("");
  const [templateid, setTemplate] = useState<number>(1);

  useEffect(() => {
    if (data) {
      console.log(data);
      setFormData(data);
      setTemplate(data.template || 1); // Встановлюємо шаблон, якщо є
      setId(data.id);
    }
  }, [data]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleTemplateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { value } = e.target;
  setFormData((prev) => ({
    ...prev,
    template: Number(value), // Змінюємо template всередині formData
  }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = { ...formData, id, userId, templateid };
    console.log('Payload:', payload);
    try {
      const response = await axios.put(`http://localhost:8080/resumes`, payload, {
        headers: { 'Content-Type': 'application/json' },
      });
      console.log('Resume updated successfully:', response.data);
      alert('Resume updated successfully!');
      navigate('/profile');
    } catch (error) {
      console.error('Error updating resume:', error);
      alert('Failed to update the resume.');
    }
  };

  const handleBack = () => {
    navigate(-1);
  };

  return (
    <form className="resume-form" onSubmit={handleSubmit}>
      <h2 className="title">Edit Resume</h2>
      <div className="form-group">
        <label>Choose the template:</label>
        <select value={templateid} onChange={handleTemplateChange}>
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
      <div>
      <button onClick={handleBack} className="btn-back">
            Back
          </button>
      <button type="submit" className="btn-save-resume">Save Changes</button>

      </div>
    </form>
  );
};

export default EditResume;
