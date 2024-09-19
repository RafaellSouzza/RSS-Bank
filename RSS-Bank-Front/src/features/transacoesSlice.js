import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  transacoes: [],
};

const transacoesSlice = createSlice({
  name: 'transacoes',
  initialState,
  reducers: {
    realizarTransacao: (state, action) => {
      state.transacoes.push(action.payload);
    },

    removerTransacao: (state, action) => {
      state.transacoes = state.transacoes.filter(
        (transacao) => transacao.id !== action.payload
      );
    },
  },
});

export const { realizarTransacao, removerTransacao } = transacoesSlice.actions;

export default transacoesSlice.reducer;
