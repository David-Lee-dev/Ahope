import Header from '@/shared/header/ui';
import { Blockquote, DarkThemeToggle } from 'flowbite-react';

export default function Home() {
  return (
    <div className='relative h-screen w-screen dark:bg-gray-800'>
      <Header activeTap='Home' />
      <div className='mt-20 flex flex-col items-center justify-center'>
        <Blockquote>We never store your data.</Blockquote>
        <Blockquote className='mt-10'>If you close the window without completing the task,</Blockquote>
        <Blockquote>all data and work will be lost.</Blockquote>
      </div>
    </div>
  );
}
