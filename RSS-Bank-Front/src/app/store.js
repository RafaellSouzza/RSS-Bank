import { configureStore } from '@reduxjs/toolkit';
import contasReducer from '../features/contasSlice';
import transacoesReducer from '../features/transacoesSlice';

export const store = configureStore({
  reducer: {
    contas: contasReducer,
    transacoes: transacoesReducer,
  },
});
