import React, { useEffect, useState } from 'react';
import { fetchAllUsers } from '../api/users';

interface User {
  id: number;
  username: string;
  email: string;
  password: string;
}

const UserList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadUsers = async () => {
      try {
        const data = await fetchAllUsers();
        setUsers(data);
      } catch (err) {
        setError("Failed to upload users.");
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  if (loading) return <div>download...</div>;
  if (error) return <div>{error}</div>;
  
  return (
    <div className="userlist-container">
      <h1>User list</h1>
      <ul>
        {users.map(user => (
          <li key={user.id}>
            {user.username} ({user.email})
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserList;

