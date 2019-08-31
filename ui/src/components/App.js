import React from 'react';
import Header from './Header';
import ContractTable from './ContractTable';

export const App = (props) => (
    <div>
      <Header/>
      <ContractTable contracts={[]}/>
    </div>
)