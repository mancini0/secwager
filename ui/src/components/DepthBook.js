import React from 'react';

const DepthBook = (props) => (  

    <div>
      <h4>market depth (top 5 levels)</h4>
  
      <div className='row'>
       <div className='column'>
        <table>
          <caption style={{ color: 'red', fontSize: 'large' }}>asks</caption>
          <tr>
            <th>price</th>
            <th>size</th>
          </tr>
          {(props.depth.asks).map((ask, i) =>
  
            <tr key={ask}>
              <td>{(ask / 100).toFixed(2)}</td>
              <td>{props.depth.ask_sizes[i]}</td>
            </tr>)}
        </table>
       </div>
  
        <div className='verticalLine column' />
  
       <div className='column'>     
        <table>
          <caption style={{ color: 'green', fontSize: 'large' }}>bids</caption>
          <tr>
            <th>price</th>
            <th>size</th>
          </tr>
          {(props.depth.bids).map((bid, i) =>
  
            <tr key={bid}>
              <td>{(bid / 100).toFixed(2)}</td>
              <td>{props.depth.bid_sizes[i]}</td>
            </tr>)}
        </table>
        </div>
      </div>
    </div>);
  
  
  export default DepthBook;