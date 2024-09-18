import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  contas: [],
};

const contasSlice = createSlice({
  name: 'contas',
  initialState,
  reducers: {
    adicionarConta: (state, action) => {
      state.contas.push(action.payload);
    },
    removerConta: (state, action) => {
      state.contas = state.contas.filter(conta => conta.id !== action.payload);
    },
  },
});

export const { adicionarConta, removerConta } = contasSlice.actions;
export default contasSlice.reducer;
