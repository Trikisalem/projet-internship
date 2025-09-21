import { useState, useEffect } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Textarea } from "./ui/textarea";
import { Badge } from "./ui/badge";
import { Separator } from "./ui/separator";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "./ui/dialog";
import { 
  SaveIcon, 
  EyeIcon, 
  EditIcon, 
  ShareIcon, 
  TagIcon, 
  PlusIcon,
  XIcon,
  LinkIcon,
  UserPlusIcon,
  GlobeIcon,
  LockIcon,
  Users2Icon
} from "lucide-react";

interface Note {
  id: string;
  title: string;
  content: string;
  tags: string[];
  visibility: "PRIVATE" | "SHARED" | "PUBLIC";
  updatedAt: string;
  createdAt: string;
}

interface Props {
  note: Note;
  onUpdateNote: (note: Note) => void;
}

export const NoteEditor = ({ note, onUpdateNote }: Props) => {
  const [editedNote, setEditedNote] = useState(note);
  const [activeTab, setActiveTab] = useState("edit");
  const [newTag, setNewTag] = useState("");
  const [shareEmail, setShareEmail] = useState("");
  const [hasUnsavedChanges, setHasUnsavedChanges] = useState(false);

  useEffect(() => {
    setEditedNote(note);
    setHasUnsavedChanges(false);
  }, [note.id]);

  useEffect(() => {
    const hasChanges = 
      editedNote.title !== note.title ||
      editedNote.content !== note.content ||
      editedNote.visibility !== note.visibility ||
      JSON.stringify(editedNote.tags) !== JSON.stringify(note.tags);
    
    setHasUnsavedChanges(hasChanges);
  }, [editedNote, note]);

  const handleSave = () => {
    onUpdateNote(editedNote);
    setHasUnsavedChanges(false);
  };

  const addTag = () => {
    if (newTag.trim() && !editedNote.tags.includes(newTag.trim())) {
      setEditedNote({
        ...editedNote,
        tags: [...editedNote.tags, newTag.trim()]
      });
      setNewTag("");
    }
  };

  const removeTag = (tagToRemove: string) => {
    setEditedNote({
      ...editedNote,
      tags: editedNote.tags.filter(tag => tag !== tagToRemove)
    });
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && (e.metaKey || e.ctrlKey)) {
      handleSave();
    }
  };

  const renderMarkdown = (content: string) => {
    return content
      .split('\n')
      .map((line, index) => {
        // Headers
        if (line.startsWith('# ')) {
          return <h1 key={index} className="text-3xl font-bold mb-4 mt-6">{line.slice(2)}</h1>;
        }
        if (line.startsWith('## ')) {
          return <h2 key={index} className="text-2xl font-semibold mb-3 mt-5">{line.slice(3)}</h2>;
        }
        if (line.startsWith('### ')) {
          return <h3 key={index} className="text-xl font-semibold mb-2 mt-4">{line.slice(4)}</h3>;
        }
        
        // Bold and italic
        let processedLine = line
          .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
          .replace(/\*(.*?)\*/g, '<em>$1</em>')
          .replace(/`(.*?)`/g, '<code class="bg-muted px-1 py-0.5 rounded text-sm">$1</code>');
        
        // Checkboxes
        if (line.includes('- [ ]') || line.includes('- [x]')) {
          const isChecked = line.includes('- [x]');
          const text = line.replace(/- \[[ x]\] /, '');
          return (
            <div key={index} className="flex items-center space-x-2 mb-1">
              <input type="checkbox" checked={isChecked} readOnly className="rounded" />
              <span className={isChecked ? 'line-through text-muted-foreground' : ''}>{text}</span>
            </div>
          );
        }
        
        // Regular paragraphs
        if (line.trim()) {
          return (
            <p 
              key={index} 
              className="mb-2 leading-relaxed"
              dangerouslySetInnerHTML={{ __html: processedLine }}
            />
          );
        }
        
        return <br key={index} />;
      });
  };

  const getVisibilityIcon = (visibility: string) => {
    switch (visibility) {
      case "PRIVATE":
        return <LockIcon className="w-4 h-4" />;
      case "SHARED":
        return <Users2Icon className="w-4 h-4" />;
      case "PUBLIC":
        return <GlobeIcon className="w-4 h-4" />;
      default:
        return <LockIcon className="w-4 h-4" />;
    }
  };

  return (
    <div className="flex flex-col h-full bg-editor">
      {/* Header */}
      <div className="border-b border-border bg-card">
        <div className="p-4 pb-2">
          <Input
            value={editedNote.title}
            onChange={(e) => setEditedNote({ ...editedNote, title: e.target.value })}
            className="text-2xl font-bold border-none bg-transparent p-0 focus-visible:ring-0 focus-visible:ring-offset-0"
            placeholder="Untitled Note"
            onKeyPress={handleKeyPress}
          />
        </div>
        
        <div className="px-4 pb-4 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            {/* Tags */}
            <div className="flex items-center space-x-2">
              <TagIcon className="w-4 h-4 text-muted-foreground" />
              <div className="flex flex-wrap gap-1">
                {editedNote.tags.map((tag) => (
                  <Badge key={tag} variant="secondary" className="text-xs">
                    {tag}
                    <button
                      onClick={() => removeTag(tag)}
                      className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                    >
                      <XIcon className="w-2 h-2" />
                    </button>
                  </Badge>
                ))}
                <div className="flex items-center space-x-1">
                  <Input
                    value={newTag}
                    onChange={(e) => setNewTag(e.target.value)}
                    onKeyPress={(e) => e.key === "Enter" && addTag()}
                    placeholder="Add tag"
                    className="h-6 text-xs w-20"
                  />
                  <Button size="sm" variant="ghost" onClick={addTag} className="h-6 w-6 p-0">
                    <PlusIcon className="w-3 h-3" />
                  </Button>
                </div>
              </div>
            </div>
            
            {/* Visibility */}
            <div className="flex items-center space-x-2">
              <Select
                value={editedNote.visibility}
                onValueChange={(value: "PRIVATE" | "SHARED" | "PUBLIC") => 
                  setEditedNote({ ...editedNote, visibility: value })
                }
              >
                <SelectTrigger className="w-32 h-8">
                  <div className="flex items-center space-x-2">
                    {getVisibilityIcon(editedNote.visibility)}
                    <SelectValue />
                  </div>
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PRIVATE">
                    <div className="flex items-center space-x-2">
                      <LockIcon className="w-4 h-4" />
                      <span>Private</span>
                    </div>
                  </SelectItem>
                  <SelectItem value="SHARED">
                    <div className="flex items-center space-x-2">
                      <Users2Icon className="w-4 h-4" />
                      <span>Shared</span>
                    </div>
                  </SelectItem>
                  <SelectItem value="PUBLIC">
                    <div className="flex items-center space-x-2">
                      <GlobeIcon className="w-4 h-4" />
                      <span>Public</span>
                    </div>
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
          
          <div className="flex items-center space-x-2">
            <Dialog>
              <DialogTrigger asChild>
                <Button variant="outline" size="sm">
                  <ShareIcon className="w-4 h-4 mr-2" />
                  Share
                </Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Share Note</DialogTitle>
                  <DialogDescription>
                    Choose how you want to share this note with others.
                  </DialogDescription>
                </DialogHeader>
                
                <div className="space-y-4">
                  <div>
                    <h4 className="font-medium mb-2 flex items-center">
                      <UserPlusIcon className="w-4 h-4 mr-2" />
                      Share with user
                    </h4>
                    <div className="flex space-x-2">
                      <Input
                        placeholder="Enter email address"
                        value={shareEmail}
                        onChange={(e) => setShareEmail(e.target.value)}
                      />
                      <Button>Share</Button>
                    </div>
                  </div>
                  
                  <Separator />
                  
                  <div>
                    <h4 className="font-medium mb-2 flex items-center">
                      <LinkIcon className="w-4 h-4 mr-2" />
                      Public link
                    </h4>
                    <div className="flex space-x-2">
                      <Input
                        readOnly
                        value="https://notes-suite.com/p/abc123"
                        className="bg-muted"
                      />
                      <Button variant="outline">Copy</Button>
                    </div>
                    <p className="text-xs text-muted-foreground mt-1">
                      Anyone with this link can view the note
                    </p>
                  </div>
                </div>
              </DialogContent>
            </Dialog>
            
            <Button 
              onClick={handleSave}
              disabled={!hasUnsavedChanges}
              className="bg-gradient-primary hover:opacity-90 transition-opacity"
            >
              <SaveIcon className="w-4 h-4 mr-2" />
              Save
            </Button>
          </div>
        </div>
      </div>
      
      {/* Editor */}
      <div className="flex-1 overflow-hidden">
        <Tabs value={activeTab} onValueChange={setActiveTab} className="h-full flex flex-col">
          <div className="border-b border-border bg-card px-4">
            <TabsList className="grid w-fit grid-cols-2">
              <TabsTrigger value="edit" className="flex items-center space-x-2">
                <EditIcon className="w-4 h-4" />
                <span>Edit</span>
              </TabsTrigger>
              <TabsTrigger value="preview" className="flex items-center space-x-2">
                <EyeIcon className="w-4 h-4" />
                <span>Preview</span>
              </TabsTrigger>
            </TabsList>
          </div>
          
          <TabsContent value="edit" className="flex-1 m-0 p-0">
            <Textarea
              value={editedNote.content}
              onChange={(e) => setEditedNote({ ...editedNote, content: e.target.value })}
              placeholder="Start writing in Markdown..."
              className="h-full border-none bg-editor resize-none focus-visible:ring-0 focus-visible:ring-offset-0 font-mono text-sm leading-relaxed"
              onKeyPress={handleKeyPress}
            />
          </TabsContent>
          
          <TabsContent value="preview" className="flex-1 m-0 p-0">
            <div className="h-full overflow-y-auto p-6 bg-background">
              <div className="max-w-3xl mx-auto prose prose-gray dark:prose-invert">
                {renderMarkdown(editedNote.content)}
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </div>
      
      {hasUnsavedChanges && (
        <div className="border-t border-border bg-card px-4 py-2">
          <div className="flex items-center justify-between text-sm">
            <span className="text-muted-foreground">You have unsaved changes</span>
            <div className="text-xs text-muted-foreground">
              Press Ctrl+Enter to save
            </div>
          </div>
        </div>
      )}
    </div>
  );
};