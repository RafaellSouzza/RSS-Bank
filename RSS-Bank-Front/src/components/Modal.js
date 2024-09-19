import React from 'react';

function Modal({ isOpen, onClose, children }) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="fixed inset-0 bg-black opacity-50" onClick={onClose}></div>

      <div className="bg-white rounded-lg shadow-lg z-10 p-6 w-11/12 max-w-md text-black">
        {children}
      </div>
    </div>
  );
}

export default Modal;
