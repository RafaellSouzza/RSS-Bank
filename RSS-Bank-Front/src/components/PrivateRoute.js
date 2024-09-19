import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

function PrivateRoute({ children }) {
  const { usuario } = useContext(AuthContext);

  if (usuario === null) {
    return <Navigate to="/login" />;
  }

  return children;
}

export default PrivateRoute;
